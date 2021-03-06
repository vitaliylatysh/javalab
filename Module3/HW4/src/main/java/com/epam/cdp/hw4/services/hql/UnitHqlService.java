package com.epam.cdp.hw4.services.hql;

import com.epam.cdp.hw4.models.Unit;
import com.epam.cdp.hw4.repositories.impl.hql.UnitHqlDao;

public class UnitHqlService {

    private UnitHqlDao unitHqlDao;

    public UnitHqlService(UnitHqlDao unitHqlDao) {
        this.unitHqlDao = unitHqlDao;
    }

    public Unit findById(long id) {
        return unitHqlDao.findById(id);
    }
}
