/** Copyright (C) 2017 Oleksandr Los
 *  This file is part of oepool-frontend project.
 *
 *  oepool-frontend is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  oepool-frontend project is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with oepool-frontend project.  If not, see <http://www.gnu.org/licenses/>.
 */

package oepool.frontend.dao.pg;

import org.hibernate.Filter;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import oepool.frontend.dao.WalletDataDao;
import oepool.frontend.model.WalletData;

import java.util.List;

public class PgWalletDataDao implements WalletDataDao {

    @Autowired
    private SessionFactory sessionFactory;

    public void setSessionFactory(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    @Transactional(propagation = Propagation.MANDATORY)
    public List<WalletData> loadByWorkerName(String wallet, String workerName) {
        Filter filter = sessionFactory.getCurrentSession().enableFilter("workerFilter");
        filter.setParameter("workerName", workerName);
        return sessionFactory.getCurrentSession()
                .createQuery("from WalletData w " +
                        "where w.wallet = :wallet and " +
                        "(now() - to_timestamp(record_timestamp/1000)) <= '1 days'")
                .setParameter("wallet", wallet)
                .list();
    }

    @Override
    @Transactional(propagation = Propagation.MANDATORY)
    public List<WalletData> loadAllData() {
        return null;
    }

    @Override
    @Transactional(propagation = Propagation.MANDATORY)
    public List<WalletData> loadWallets() {
        return sessionFactory.getCurrentSession()
                .createQuery("select w.wallet from WalletData w group by w.wallet")
                .getResultList();
    }

    @Override
    @Transactional(propagation = Propagation.MANDATORY)
    public List<WalletData> loadData(String wallet, long startTimeStamp, long endTimeStamp) {
        return sessionFactory.getCurrentSession()
                .createQuery("from WalletData w " +
                        "where w.wallet = :wallet and " +
                        "record_timestamp > :startTimeStamp and " +
                        "record_timestamp <= :endTimeStamp")
                .setParameter("wallet", wallet)
                .setParameter("startTimeStamp", startTimeStamp)
                .setParameter("endTimeStamp", endTimeStamp)
                .list();
    }
}
