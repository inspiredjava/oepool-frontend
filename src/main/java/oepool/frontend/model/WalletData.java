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

import org.hibernate.annotations.Filter;
import org.hibernate.annotations.SortNatural;

import javax.persistence.*;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.*;

@Entity
@Table (name = "wallet")
public class WalletData implements Comparable<WalletData> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "wallet")
    private String wallet;
    @Column(name = "record_timestamp")
    private long record_timestamp;
    @Column(name = "current_hashrate")
    private Long currentHashrate;
    @Column(name = "hashrate")
    private Long hashrate;
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinTable(
            name = "worker_to_wallet",
            joinColumns = {@JoinColumn(name = "wallet_id", referencedColumnName = "id")},
            inverseJoinColumns = {@JoinColumn(name = "worker_id", referencedColumnName = "id", unique = true)})
    @Filter(name="workerFilter", condition = "name = :workerName")
    @SortNatural
    private SortedSet<Worker> workerSet = new TreeSet<>();
    @Column(name = "workers_offline")
    private Integer workersOffline;
    @Column(name = "workers_online")
    private Integer workersOnline;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getWallet() {
        return wallet;
    }

    public void setWallet(String wallet) {
        this.wallet = wallet;
    }

    public long getRecord_timestamp() {
        return record_timestamp;
    }

    public void setRecord_timestamp(long record_timestamp) {
        this.record_timestamp = record_timestamp;
    }

    public Long getCurrentHashrate() {
        return currentHashrate;
    }

    public void setCurrentHashrate(Long currentHashrate) {
        this.currentHashrate = currentHashrate;
    }

    public Long getHashrate() {
        return hashrate;
    }

    public void setHashrate(Long hashrate) {
        this.hashrate = hashrate;
    }

    public SortedSet<Worker> getWorkerSet() {
        return workerSet;
    }

    public void setWorkerSet(SortedSet<Worker> workerSet) {
        this.workerSet = workerSet;
    }

    public Integer getWorkersOffline() {
        return workersOffline;
    }

    public void setWorkersOffline(Integer workersOffline) {
        this.workersOffline = workersOffline;
    }

    public Integer getWorkersOnline() {
        return workersOnline;
    }

    public void setWorkersOnline(Integer workersOnline) {
        this.workersOnline = workersOnline;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        WalletData that = (WalletData) o;

        return wallet.equals(that.wallet);
    }

    @Override
    public int hashCode() {
        return wallet.hashCode();
    }

    @Override
    public String toString() {
        return "WalletDao{" +
                "wallet='" + wallet + '\'' +
                ", record_timestamp=" + record_timestamp +
                ", currentHashrate=" + currentHashrate +
                ", hashrate=" + hashrate +
                ", workerSet=" + workerSet +
                ", workersOffline=" + workersOffline +
                ", workersOnline=" + workersOnline +
                '}';
    }

    @Override
    public int compareTo(WalletData o) {
        return id.compareTo(o.id);
    }
}
