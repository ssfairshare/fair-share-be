package me.shaneslone.fairshare.services;

import me.shaneslone.fairshare.models.Household;

import java.util.List;

public interface HouseholdService {
    List<Household> findAll();
    Household findByHouseholdId(long id);
    Household findByHouseholdKey(String key);
    void delete(long id);
    Household save(Household household);
    void deleteAll();
}
