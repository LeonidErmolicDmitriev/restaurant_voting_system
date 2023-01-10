package com.github.leonidermolicdmitriev.restaurant_voting_system.web.vote;

import com.github.leonidermolicdmitriev.restaurant_voting_system.model.Restaurant;
import com.github.leonidermolicdmitriev.restaurant_voting_system.model.Vote;
import com.github.leonidermolicdmitriev.restaurant_voting_system.web.AuthUser;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import com.github.leonidermolicdmitriev.restaurant_voting_system.error.IllegalRequestDataException;
import com.github.leonidermolicdmitriev.restaurant_voting_system.repository.VoteRepository;
import com.github.leonidermolicdmitriev.restaurant_voting_system.to.VoteTo;
import com.github.leonidermolicdmitriev.restaurant_voting_system.util.VoteUtil;

import javax.persistence.EntityManager;
import java.net.URI;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@RestController
@RequestMapping(value = VoteRestController.REST_URL, produces = MediaType.APPLICATION_JSON_VALUE)
@Slf4j
public class VoteRestController {

    static final String REST_URL = "/api/votes";

    private final VoteRepository voteRepository;

    static LocalTime maxUpdateTime = LocalTime.of(11, 0);

    private final EntityManager entityManager;

    public VoteRestController(VoteRepository voteRepository, EntityManager entityManager) {
        this.voteRepository = voteRepository;
        this.entityManager = entityManager;
    }

    @GetMapping("/by-date")
    public List<VoteTo> getByDate(@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        log.info("getByDate");
        return getAllByDate(date);
    }

    @GetMapping
    public List<VoteTo> getByToday() {
        LocalDate date = LocalDate.now();
        log.info("getByToday");
        return getAllByDate(date);
    }

    private List<VoteTo> getAllByDate(LocalDate date) {
        log.info("get votes by date {}", date);
        return VoteUtil.votesToTos(voteRepository.getAllByDate(date));
    }

    @GetMapping(value = "/profile", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<VoteTo> getByUser(@AuthenticationPrincipal AuthUser authUser) {
        int userId = authUser.id();
        log.info("get current vote for user id={}", userId);
        return ResponseEntity.of(VoteUtil.getTo(voteRepository.getByDateAndUserId(LocalDate.now(), userId)));
    }

    @GetMapping("/{id}")
    public ResponseEntity<VoteTo> get(@PathVariable int id) {
        log.info("get dish by id {}", id);
        return ResponseEntity.of(VoteUtil.getTo(voteRepository.getByIdWithRestaurant(id)));
    }

    @PostMapping(value = "/profile", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<VoteTo> createWithLocation(@RequestBody Integer restaurantId,
                                                     @AuthenticationPrincipal AuthUser authUser) {
        log.info("create vote for user id {} with restaurant id={}", authUser.id(), restaurantId);
        Vote newVote = saveVote(restaurantId, authUser, true);
        URI uriOfNewResource = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path(REST_URL + "/profile")
                .buildAndExpand(newVote.getId()).toUri();
        return ResponseEntity.created(uriOfNewResource).body(VoteUtil.createTo(newVote));
    }

    @PutMapping(value = "/profile", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void update(@RequestBody Integer restaurantId, @AuthenticationPrincipal AuthUser authUser) {
        log.info("update vote for user id {} with restaurant id={}", authUser.id(), restaurantId);
        saveVote(restaurantId, authUser, false);
    }

    private Vote saveVote(Integer restaurantId, AuthUser authUser, boolean isCreateOp) {
        LocalDate today = LocalDate.now();
        Restaurant restaurant = entityManager.find(Restaurant.class, restaurantId);
        Vote newVote = new Vote(null, authUser.getUser(), restaurant, today);
        Vote vote = voteRepository.getByDateAndUserId(today, authUser.id()).orElse(newVote);
        if (vote.isNew() != isCreateOp) {
            throw new IllegalRequestDataException("Please call the " + (isCreateOp ? "update" : "create") + " method");
        }
        if (!isCreateOp && (maxUpdateTime.equals(LocalTime.MIN)
                || (!LocalTime.now().isBefore(maxUpdateTime) && !maxUpdateTime.equals(LocalTime.MAX)))) {
            throw new IllegalRequestDataException("No more voting updates is allowed");
        }
        newVote.setId(vote.getId());
        return voteRepository.save(newVote);
    }

    public static void setMaxUpdateTime(LocalTime time) {
        maxUpdateTime = time;
    }
}