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

import org.hibernate.annotations.*;

import javax.persistence.*;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "worker")
@FilterDef(name="workerFilter", parameters=@ParamDef(name="workerName", type = "text"))
@Filter(name="workerFilter", condition = "name = :name")
public class Worker implements Comparable<Worker>{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "name")
    @NaturalId
    private String name;
    @Column(name = "last_beat")
    private Long lastBeat;
    @Column(name = "hr")
    private Long hr;
    @Column(name = "offline")
    private Boolean offline;
    @Column(name = "hr2")
    private Long hr2;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public int compareTo(Worker o) {
        return name.compareTo(o.getName());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Worker worker = (Worker) o;

        return name != null ? name.equals(worker.name) : worker.name == null;
    }

    @Override
    public int hashCode() {
        return name != null ? name.hashCode() : 0;
    }

    @Override
    public String toString() {
        return "Worker{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", lastBeat=" + lastBeat +
                ", hr=" + hr +
                ", offline=" + offline +
                ", hr2=" + hr2 +
                '}';
    }

    public void setHr2(Long hr2) {
        this.hr2 = hr2;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getLastBeat() {
        return lastBeat;
    }

    public void setLastBeat(Long lastBeat) {
        this.lastBeat = lastBeat;
    }

    public Long getHr() {
        return hr;
    }

    public void setHr(Long hr) {
        this.hr = hr;
    }

    public Boolean getOffline() {
        return offline;
    }

    public void setOffline(Boolean offline) {
        this.offline = offline;
    }

    public long getHr2() {
        return hr2;
    }

    public void setHr2(long hr2) {
        this.hr2 = hr2;
    }
}
