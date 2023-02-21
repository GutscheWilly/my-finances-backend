package com.gutsche.myFinances.model.entity;

import com.gutsche.myFinances.model.entity.enums.LaunchStatus;
import com.gutsche.myFinances.model.entity.enums.LaunchType;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.jpa.convert.threeten.Jsr310JpaConverters;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
@ToString
@Builder
@Table(name = "launch", schema = "finances")
public class Launch {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "description")
    private String description;

    @Column(name = "month")
    private Integer month;

    @Column(name = "year")
    private Integer year;

    @Column(name = "value")
    private BigDecimal value;

    @Column(name = "type")
    @Enumerated(value = EnumType.STRING)
    private LaunchType type;

    @Column(name = "status")
    @Enumerated(value = EnumType.STRING)
    private LaunchStatus status;

    @Column(name = "registration_date")
    @Convert(converter = Jsr310JpaConverters.LocalDateConverter.class)
    private LocalDate registrationDate;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
}
