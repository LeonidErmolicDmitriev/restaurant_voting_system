package com.github.leonidermolicdmitriev.restaurant_voting_system.to;

import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.Value;

@Value
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class DishTo extends NamedTo {
    public DishTo(Integer id, String name) {
        super(id, name);
    }
}
