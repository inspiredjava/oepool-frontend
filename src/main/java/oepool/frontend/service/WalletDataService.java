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

package oepool.frontend.service;

import org.springframework.beans.factory.annotation.Autowired;
import oepool.frontend.dao.WalletDataDao;
import oepool.frontend.model.WalletData;

import javax.transaction.Transactional;
import java.util.List;

public class WalletDataService {

    @Autowired
    private WalletDataDao walletDataDao;

    @Transactional
    public List<WalletData> loadData(String wallet, long startTimeStamp, long endTimeStamp) {
        return walletDataDao.loadData(wallet, startTimeStamp, endTimeStamp);
    }

    @Transactional
    public List<WalletData> loadByWorkerName(String wallet, String workerName) {
        return walletDataDao.loadByWorkerName(wallet, workerName);
    }

    public void setWalletDataDao(WalletDataDao walletDataDao) {
        this.walletDataDao = walletDataDao;
    }

    @Transactional
    public List<WalletData> loadAllData() {
        return walletDataDao.loadAllData();
    }

    @Transactional
    public List<WalletData> loadWallets() {
        return walletDataDao.loadWallets();
    }

}
