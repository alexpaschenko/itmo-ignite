package ru.ifmo.escience.ignite.week5.lab;

import org.apache.ignite.cache.query.annotations.QuerySqlField;

public class Client {
    @QuerySqlField(index = true)
    private final ClientKey clientKey;
    @QuerySqlField
    private final String fullName;


    public Client(ClientKey clientKey, String fullName) {
        this.clientKey = clientKey;
        this.fullName = fullName;
    }

    public ClientKey getClientKey() {
        return clientKey;
    }

    public String getFullName() {
        return fullName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Client client = (Client) o;

        if (clientKey != null ? !clientKey.equals(client.clientKey) : client.clientKey != null) return false;
        return fullName != null ? fullName.equals(client.fullName) : client.fullName == null;
    }

    @Override
    public int hashCode() {
        int result = clientKey != null ? clientKey.hashCode() : 0;
        result = 31 * result + (fullName != null ? fullName.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Client{" +
                "clientKey=" + clientKey +
                ", fullName='" + fullName + '\'' +
                '}';
    }
}
