package ru.itmo.soa.soabe.servlet;

import ru.itmo.soa.soabe.entity.Car;
import ru.itmo.soa.soabe.entity.Coordinates;
import ru.itmo.soa.soabe.entity.HumanBeing;
import ru.itmo.soa.soabe.entity.WeaponType;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;

public class HumanBeingRequestParams {
    public final String name;
    public final Long minutesOfWaiting;
    public final Boolean realHero;
    public final Boolean hasToothpick;
    public final Long impactSpeed;
    public final String soundtrackName;
    public final WeaponType weaponType;
    public final String carName;
    public final Boolean carCool;
    public final Float coordinatesX;
    public final Float coordinatesY;
    public final String creationDate;
    public final String sorting;
    public final int pageIdx;
    public final int pageSize;

    HumanBeingRequestParams(
            String name,
            String minutesOfWaiting,
            String realHero,
            String hasToothpick,
            String impactSpeed,
            String soundtrackName,
            String weaponType,
            String carName,
            String carCool,
            String coordinatesX,
            String coordinatesY,
            String creationDate,
            String sorting,
            String pageIdx,
            String pageSize
    ) {
        this.sorting = sorting;
        this.name = name;
        this.minutesOfWaiting = minutesOfWaiting == null ? null : Long.parseLong(minutesOfWaiting);
        this.realHero = realHero == null ? null : Boolean.parseBoolean(realHero);
        this.hasToothpick = hasToothpick == null ? null : Boolean.parseBoolean(hasToothpick);
        this.impactSpeed = impactSpeed == null ? null : Long.parseLong(impactSpeed);
        this.soundtrackName = soundtrackName;
        this.weaponType = weaponType == null ? null : WeaponType.valueOf(weaponType);
        this.carName = carName;
        this.carCool = carCool == null ? null : Boolean.parseBoolean(carCool);
        this.coordinatesX = coordinatesX == null ? null : Float.parseFloat(coordinatesX);
        this.coordinatesY = coordinatesY == null ? null : Float.parseFloat(coordinatesY);
        this.creationDate = creationDate;
        this.pageIdx = pageIdx == null ? 1 : Integer.parseInt(pageIdx);
        this.pageSize = pageSize == null ? 5 : Integer.parseInt(pageSize);
    }

    private String like(String val) {
        return "%" + val + "%";
    }

    public List<javax.persistence.criteria.Predicate> getPredicates(
            CriteriaBuilder cb,
            Root<HumanBeing> root,
            Join<HumanBeing, Car> join,
            Join<HumanBeing, Coordinates> joinCoordinates
    ) {
        List<javax.persistence.criteria.Predicate> predicates = new ArrayList<>();
        if (name != null) {
            predicates.add(cb.like(root.get("name"), like(name)));
        }
        if (minutesOfWaiting != null) {
            predicates.add(cb.equal(root.get("minutesOfWaiting"), minutesOfWaiting));
        }
        if (realHero != null) {
            predicates.add(cb.equal(root.get("realHero"), realHero));
        }
        if (hasToothpick != null) {
            predicates.add(cb.equal(root.get("hasToothpick"), hasToothpick));
        }
        if (impactSpeed != null) {
            predicates.add(cb.equal(root.get("impactSpeed"), impactSpeed));
        }
        if (soundtrackName != null) {
            predicates.add(cb.like(root.get("soundtrackName"), like(soundtrackName)));
        }
        if (weaponType != null) {
            predicates.add(cb.equal(root.get("weaponType"), weaponType));
        }
        if (carName != null) {
            predicates.add(cb.like(join.get("name"), like(carName)));
        }
        if (carCool != null) {
            predicates.add(cb.equal(join.get("cool"), carCool));
        }
        if (coordinatesX != null) {
            predicates.add(cb.equal(joinCoordinates.get("x"), coordinatesX));
        }
        if (coordinatesY != null) {
            predicates.add(cb.equal(joinCoordinates.get("y"), coordinatesY));
        }
        if (creationDate != null) {
            predicates.add(cb.equal(root.get("creationDate"), creationDate));
        }
        return predicates;
    }
}