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

package oepool.frontend.web;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import oepool.frontend.model.*;
import oepool.frontend.service.WalletDataService;

import java.util.*;
import java.util.Date;

@Controller
public class MainController {

    private final Logger LOGGER = LogManager.getLogger(this.getClass().getName());

    @Autowired
    private WalletDataService walletDataService;

    private Set<WalletData> loadWalletOverallData(String walletAddress, long startTimeStamp, long endTimeStamp) {
        return new TreeSet<>(walletDataService.loadData(walletAddress, startTimeStamp, endTimeStamp));
    }

    private Set<WorkerStatistic> extractWorkers(Set<WalletData> walletOverallData) {
        // TODO sorting with different strategy
        Set<WorkerStatistic> workerStatisticSet = new TreeSet<>();

        Iterator <WalletData> walletOverallDataIterator =  walletOverallData.iterator();
        while (walletOverallDataIterator.hasNext()) {
            WalletData walletData = walletOverallDataIterator.next();
            Iterator<Worker> workerIterator = walletData.getWorkerSet().iterator();
            while (workerIterator.hasNext()) {
                Worker worker = workerIterator.next();

                WorkerStatistic currentWorkerStatistic = new WorkerStatistic();
                currentWorkerStatistic.setName(worker.getName());

                if (!workerStatisticSet.contains(currentWorkerStatistic)) {
                    currentWorkerStatistic.getHr().add(new HrPoint(walletData.getRecord_timestamp(), worker.getHr()));
                    currentWorkerStatistic.getHr2().add(new HrPoint(walletData.getRecord_timestamp(), worker.getHr2()));
                    currentWorkerStatistic.setLastBeat(worker.getLastBeat());
                    currentWorkerStatistic.setOffline(worker.getOffline());
                    // if worker online than set lastOnline point
                    if (!worker.getOffline()) {
                        currentWorkerStatistic.setLastOnlinePoint(walletData.getRecord_timestamp());
                        currentWorkerStatistic.setLastTimeStamp(walletData.getRecord_timestamp());
                    }
                    workerStatisticSet.add(currentWorkerStatistic);
                } else {
                    // if such worker is in the set then find it
                    Iterator<WorkerStatistic> workerStatisticIterator = workerStatisticSet.iterator();
                    while (workerStatisticIterator.hasNext()) {
                        WorkerStatistic previousWorkerStatistic = workerStatisticIterator.next();
                        if (currentWorkerStatistic.equals(previousWorkerStatistic)) {
                            previousWorkerStatistic.getHr().add(new HrPoint(walletData.getRecord_timestamp(),
                                    worker.getHr()));
                            previousWorkerStatistic.getHr2().add(new HrPoint(walletData.getRecord_timestamp(),
                                    worker.getHr2()));
                            previousWorkerStatistic.setLastBeat(worker.getLastBeat());

                            //if it's online
                            if (!worker.getOffline()) {
                                if (previousWorkerStatistic.getLastTimeStamp() != 0) {
                                    previousWorkerStatistic.addActivePeriod(walletData.getRecord_timestamp() -
                                            previousWorkerStatistic.getLastTimeStamp());
                                }

                                // if worker was offline than move lastOnline point to new location on time axis
                                if (previousWorkerStatistic.getOffline()) {
                                    previousWorkerStatistic.setLastOnlinePoint(walletData.getRecord_timestamp());
                                }
                                // create new Period from lastOnlinePoint to current record timestamp
                                previousWorkerStatistic.setOnlinePeriod(walletData.getRecord_timestamp() -
                                        previousWorkerStatistic.getLastOnlinePoint());
                            }
                            // if worker fall-off offline than drop online period
                            else {
                                previousWorkerStatistic.setOnlinePeriod(0);
                            }
                            previousWorkerStatistic.setLastTimeStamp(walletData.getRecord_timestamp());
                            previousWorkerStatistic.setOffline(worker.getOffline());
                        }
                    }
                }
            }
        }

        return workerStatisticSet;
    }

    @RequestMapping(value = "/{wallet}/{worker}", method = RequestMethod.GET)
    public ModelAndView viewWorkerDetails(@PathVariable String wallet, @PathVariable String worker)  {
        LOGGER.info("Request for worker details for " + worker);
        ModelAndView modelAndView = new ModelAndView("workerdetails");
        modelAndView.addObject("wallet", wallet);
        modelAndView.addObject("workerName", worker);

        Set<WalletData> walletDataList = new TreeSet<>(walletDataService.loadByWorkerName(wallet, worker));
        Set<WorkerStatistic> workerStatistics = extractWorkers(walletDataList);
        WorkerStatistic ws = workerStatistics.iterator().next();
        modelAndView.addObject("workerStatistic", ws);

        return modelAndView;
    }

    @RequestMapping(value = "/test", method = RequestMethod.GET)
    public ModelAndView testPage() {
        ModelAndView modelAndView = new ModelAndView("test");
        List<WalletData> wallets = walletDataService.loadWallets();
        modelAndView.addObject("wallets", wallets);

        return modelAndView;
    }

    @RequestMapping(value="/", method = RequestMethod.GET)
    public ModelAndView WalletData(@RequestParam(value = "walletSelector", required = false) String walletAddress,
                                   @RequestParam(value = "startDate", required = false) String startDateString,
                                   @RequestParam(value = "endDate", required = false) String endDateString) {

        List<WalletData> wallets = walletDataService.loadWallets();
        ModelAndView modelAndView = new ModelAndView("index");
        modelAndView.addObject("errorStatus", "flase");
        modelAndView.addObject("wallets", wallets);

        if (walletAddress != null) {
            try {
                Date startDate = new Date(startDateString);
                Date endDate = new Date(endDateString);

                if (startDate.getTime() >= endDate.getTime()) {
                    throw new IllegalArgumentException();
                }

                Set<WalletData> walletOverallData = loadWalletOverallData(walletAddress, startDate.getTime(), endDate.getTime());
                modelAndView.addObject("results", true);
                modelAndView.addObject("startDate", startDate.getTime());
                modelAndView.addObject("endDate", endDate.getTime());
                modelAndView.addObject("walletOverallData", walletOverallData);
                modelAndView.addObject("workerStatisticSet", extractWorkers(walletOverallData));

                return modelAndView;
            } catch (IllegalArgumentException e) {
                modelAndView.addObject("errorStatus", "true");
                modelAndView.addObject("wallets", wallets);

                return modelAndView;
            }
        }
        return modelAndView;
    }

    @RequestMapping(value = "/joingraph", method = RequestMethod.GET)
    public ModelAndView getJoinGraph(@RequestParam(value = "walletSelector", required = false) String walletAddress,
                                    @RequestParam(value = "periodSelector", required = false) Integer period) {
        ModelAndView modelAndView = new ModelAndView("joingraph");
        List<WalletData> wallets = walletDataService.loadWallets();
        modelAndView.addObject("wallets", wallets);

        if (walletAddress != null) {
            long endDate = DateTime.now().getMillis();
            long startDate = DateTime.now().minusDays(period).getMillis();
            Set<WalletData> walletOverallData = loadWalletOverallData(walletAddress, startDate, endDate);
            Set<WorkerStatistic> workerStatisticSet = extractWorkers(walletOverallData);

            modelAndView.addObject("results", true);
            modelAndView.addObject("walletOverallData", walletOverallData);
            modelAndView.addObject("workerStatisticSet", workerStatisticSet);
        } else {
            modelAndView.addObject("results", false);
        }

        return modelAndView;
    }

    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public ModelAndView getList(@RequestParam (value = "walletSelector", required = false) String walletAddress,
                                @RequestParam (value = "periodSelector", required = false) Integer period) {
        ModelAndView modelAndView = new ModelAndView("list");
        List<WalletData> wallets = walletDataService.loadWallets();
        modelAndView.addObject("wallets", wallets);

        if (walletAddress != null) {
            long endDate = DateTime.now().getMillis();
            long startDate = DateTime.now().minusDays(period).getMillis();
            Set<WalletData> walletOverallData = loadWalletOverallData(walletAddress, startDate, endDate);
            Set<WorkerStatistic> workerStatisticSet = extractWorkers(walletOverallData);

            modelAndView.addObject("walletAddress", walletAddress);
            modelAndView.addObject("results", true);
            modelAndView.addObject("walletOverallData", walletOverallData);
            modelAndView.addObject("workerStatisticSet", workerStatisticSet);
        } else {
            modelAndView.addObject("results", false);
        }

        return modelAndView;
    }

    @RequestMapping(value = "/tits", method = RequestMethod.GET)
    public String showTits() {
        return "tits";
    }

    public WalletDataService getWalletDataService() {
        return walletDataService;
    }

    public void setWalletDataService(WalletDataService walletDataService) {
        this.walletDataService = walletDataService;
    }
}
