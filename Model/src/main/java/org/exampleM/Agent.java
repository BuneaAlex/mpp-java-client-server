package org.exampleM;

import java.util.Objects;

public class Agent implements Identifiable<Integer>{

    private Integer id;

    private String agencyName;

    private String userName;

    private String password;

    public Agent(String agencyName, String userName, String password) {

        this.agencyName = agencyName;
        this.userName = userName;
        this.password = password;
    }

    public Integer getId() {
        return id;
    }

    public String getAgencyName() {
        return agencyName;
    }

    public String getUserName() {
        return userName;
    }

    public String getPassword() {
        return password;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setAgencyName(String agencyName) {
        this.agencyName = agencyName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public Integer getID() {
        return id;
    }

    @Override
    public void setID(Integer id) {
        this.id = id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Agent agent = (Agent) o;
        return Objects.equals(id, agent.id) && Objects.equals(agencyName, agent.agencyName) && Objects.equals(userName, agent.userName) && Objects.equals(password, agent.password);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, agencyName, userName, password);
    }

    @Override
    public String toString() {
        return "Agent{" +
                "id=" + id +
                ", agencyName='" + agencyName + '\'' +
                ", userName='" + userName + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}
