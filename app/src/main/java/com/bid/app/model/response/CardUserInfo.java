package com.bid.app.model.response;

import java.io.Serializable;

public class CardUserInfo implements Serializable {
    private String id;
    private String company_id;
    private String island_id;
    private String name;
    private String lat;
    private String lon;
    private String username;
    private String slug;
    private String is_archive;
    private String first_name;
    private String last_name;
    private String votes;
    private String account_update_action;
    private String platform;
    private String version;
    private String account_update_finished;
    private String detail_shared;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCompany_id() {
        return company_id;
    }

    public void setCompany_id(String company_id) {
        this.company_id = company_id;
    }

    public String getIsland_id() {
        return island_id;
    }

    public void setIsland_id(String island_id) {
        this.island_id = island_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getLon() {
        return lon;
    }

    public void setLon(String lon) {
        this.lon = lon;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getSlug() {
        return slug;
    }

    public void setSlug(String slug) {
        this.slug = slug;
    }

    public String getIs_archive() {
        return is_archive;
    }

    public void setIs_archive(String is_archive) {
        this.is_archive = is_archive;
    }

    public String getFirst_name() {
        return first_name;
    }

    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }

    public String getLast_name() {
        return last_name;
    }

    public void setLast_name(String last_name) {
        this.last_name = last_name;
    }

    public String getVotes() {
        return votes;
    }

    public void setVotes(String votes) {
        this.votes = votes;
    }

    public String getAccount_update_action() {
        return account_update_action;
    }

    public void setAccount_update_action(String account_update_action) {
        this.account_update_action = account_update_action;
    }

    public String getPlatform() {
        return platform;
    }

    public void setPlatform(String platform) {
        this.platform = platform;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getAccount_update_finished() {
        return account_update_finished;
    }

    public void setAccount_update_finished(String account_update_finished) {
        this.account_update_finished = account_update_finished;
    }

    public String getDetail_shared() {
        return detail_shared;
    }

    public void setDetail_shared(String detail_shared) {
        this.detail_shared = detail_shared;
    }
}
