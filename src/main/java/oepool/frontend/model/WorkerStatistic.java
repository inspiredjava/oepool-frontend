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

package oepool.frontend.model;

import java.util.ArrayList;
import java.util.List;

public class WorkerStatistic implements Comparable<WorkerStatistic> {

    private String name;
    private List<HrPoint> hr = new ArrayList<>();
    private List<HrPoint> hr2 = new ArrayList<>();
    private long activePeriod;
    private long onlinePeriod;
    private long lastBeat;
    private Boolean offline;
    private long lastOnlinePoint;
    private long lastTimeStamp;

    public void addActivePeriod(long period) {
        activePeriod += period;
    }

    public long getOnlinePeriodMins() {
        return onlinePeriod / 60000;
    }

    public long getIncessantlyPeriodMins() {
        return activePeriod / 60000;
    }

    public long getLastOnlinePoint() {
        return lastOnlinePoint;
    }

    public long getLastTimeStamp() {
        return lastTimeStamp;
    }

    public void setLastTimeStamp(long lastTimeStamp) {
        this.lastTimeStamp = lastTimeStamp;
    }

    public void setLastOnlinePoint(long lastOnline) {
        this.lastOnlinePoint = lastOnline;
    }

    public long getActivePeriod() {
        return activePeriod;
    }

    public void setActivePeriod(long activePeriod) {
        this.activePeriod = activePeriod;
    }

    public long getOnlinePeriod() {
        return onlinePeriod;
    }

    public void setOnlinePeriod(long onlinePeriod) {
        this.onlinePeriod = onlinePeriod;
    }

    public List<HrPoint> getHr() {
        return hr;
    }

    public void setHr(List<HrPoint> hr) {
        this.hr = hr;
    }

    public List<HrPoint> getHr2() {
        return hr2;
    }

    public void setHr2(List<HrPoint> hr2) {
        this.hr2 = hr2;
    }

    public long getLastBeat() {
        return lastBeat;
    }

    public void setLastBeat(long lastBeat) {
        this.lastBeat = lastBeat;
    }

    public Boolean getOffline() {
        return offline;
    }

    public void setOffline(Boolean offline) {
        this.offline = offline;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public int compareTo(WorkerStatistic o) {
        return name.compareTo(o.name);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        WorkerStatistic that = (WorkerStatistic) o;

        return name.equals(that.name);
    }

    @Override
    public int hashCode() {
        return name.hashCode();
    }
}
