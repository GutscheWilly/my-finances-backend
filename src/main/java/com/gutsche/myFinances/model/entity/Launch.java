package com.gutsche.myFinances.model.entity;

import com.gutsche.myFinances.model.entity.enums.LaunchStatus;
import com.gutsche.myFinances.model.entity.enums.LaunchType;
import jakarta.persistence.*;
import org.springframework.data.jpa.convert.threeten.Jsr310JpaConverters;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Objects;

@Entity
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

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getMonth() {
        return month;
    }

    public void setMonth(Integer month) {
        this.month = month;
    }

    public Integer getYear() {
        return year;
    }

    public void setYear(Integer year) {
        this.year = year;
    }

    public BigDecimal getValue() {
        return value;
    }

    public void setValue(BigDecimal value) {
        this.value = value;
    }

    public LaunchType getType() {
        return type;
    }

    public void setType(LaunchType type) {
        this.type = type;
    }

    public LaunchStatus getStatus() {
        return status;
    }

    public void setStatus(LaunchStatus status) {
        this.status = status;
    }

    public LocalDate getRegistrationDate() {
        return registrationDate;
    }

    public void setRegistrationDate(LocalDate registrationDate) {
        this.registrationDate = registrationDate;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Launch launch = (Launch) o;
        return Objects.equals(id, launch.id) && Objects.equals(description, launch.description) && Objects.equals(month, launch.month) && Objects.equals(year, launch.year) && Objects.equals(value, launch.value) && type == launch.type && status == launch.status && Objects.equals(registrationDate, launch.registrationDate) && Objects.equals(user, launch.user);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, description, month, year, value, type, status, registrationDate, user);
    }
}
