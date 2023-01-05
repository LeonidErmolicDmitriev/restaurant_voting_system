package ru.javaops.restaurant_voting_system.web.vote;

import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import ru.javaops.restaurant_voting_system.error.IllegalRequestDataException;
import ru.javaops.restaurant_voting_system.model.*;
import ru.javaops.restaurant_voting_system.repository.VoteRepository;
import ru.javaops.restaurant_voting_system.web.AuthUser;

import javax.persistence.EntityManager;
import java.net.URI;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@RestController
@RequestMapping(value = VoteRestController.REST_URL, produces = MediaType.APPLICATION_JSON_VALUE)
@Slf4j
public class VoteRestController {
    static final String REST_URL = "/api/profile/votes";
    private final VoteRepository voteRepository;
    private final LocalTime maxUpdateTime = LocalTime.of(11, 0);
    private final EntityManager entityManager;

    public VoteRestController(VoteRepository voteRepository, EntityManager entityManager) {
        this.voteRepository = voteRepository;
        this.entityManager = entityManager;
    }

    @GetMapping("/by_date")
    public List<Vote> getByDate(@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        log.info("getByDate");
        return getAllByDate(date);
    }

    @GetMapping("/today")
    public List<Vote> getByToday() {
        LocalDate date = LocalDate.now();
        log.info("getByToday");
        return getAllByDate(date);
    }

    private List<Vote> getAllByDate(LocalDate date) {
        log.info("get votes by date {}", date);
        return voteRepository.getAllByDate(date);
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public Vote getByUser(@AuthenticationPrincipal AuthUser authUser) {
        int userId = authUser.id();
        log.info("get vote for user id={}", userId);
        return voteRepository.getByDateAndUserId(LocalDate.now(), userId).orElseThrow();
    }

    @GetMapping("/{id}")
    public Vote get(@PathVariable int id) {
        log.info("get dish by id {}", id);
        return voteRepository.findById(id).orElseThrow();
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Vote> createWithLocation(@RequestParam Integer restaurantId, @AuthenticationPrincipal AuthUser authUser) {
        log.info("create vote for user id {} with restaurant id={}", authUser.id(), restaurantId);
        Vote newVote = saveVote(restaurantId, authUser, true);
        URI uriOfNewResource = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path(REST_URL + "/{id}")
                .buildAndExpand(newVote.getId()).toUri();
        return ResponseEntity.created(uriOfNewResource).body(newVote);
    }

    @PutMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void update(@RequestParam Integer restaurantId, @AuthenticationPrincipal AuthUser authUser) {
        log.info("update vote for user id {} with restaurant id={}", authUser.id(), restaurantId);
        saveVote(restaurantId, authUser, false);
    }

    private Vote saveVote(Integer restaurantId, AuthUser authUser, boolean isCreateOp){
        LocalDate today = LocalDate.now();
        Restaurant restaurant = entityManager.find(Restaurant.class, restaurantId);
        Vote newVote = new Vote(null, authUser.getUser(), restaurant, today);
        Vote vote = voteRepository.getByDateAndUserId(today, authUser.id()).orElse(newVote);
        if (vote.isNew() != isCreateOp) {
            throw new IllegalRequestDataException("Please call the " + (isCreateOp? "update": "create") + " method");
        }
        if (!isCreateOp && LocalTime.now().isBefore(maxUpdateTime)){
            throw new IllegalRequestDataException("No more voting updates is allowed");
        }
        newVote.setId(vote.id());
        return voteRepository.save(newVote);
    }
}