package com.github.leonidermolicdmitriev.restaurant_voting_system.model;

import lombok.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Entity
@Table(name = "votes")
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString(callSuper = true)
public class Vote extends BaseEntity {

    @JoinColumn(name = "user_id", nullable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    @NotNull
    @OnDelete(action = OnDeleteAction.CASCADE)
    @ToString.Exclude
    private User user;

    @JoinColumn(name = "restaurant_id", nullable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    @NotNull
    @OnDelete(action = OnDeleteAction.CASCADE)
    @ToString.Exclude
    private Restaurant restaurant;

    @Column(name = "registered_date", nullable = false)
    @NotNull
    private LocalDate registeredDate;

    public Vote(Integer id, User user, Restaurant restaurant, LocalDate registeredDate) {
        super(id);
        this.user = user;
        this.restaurant = restaurant;
        this.registeredDate = registeredDate;
    }
}
