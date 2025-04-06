package net.talor1n.passforge.entity;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.ArrayList;
import java.util.List;

@FieldDefaults(level = AccessLevel.PRIVATE)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Tag {
    String name;
    @Setter(AccessLevel.NONE)
    List<Account> accounts = new ArrayList<>();

    public Tag(String name) {
        this.name = name;
    }

    public void addAccount(Account account) {
        this.accounts.add(account);
    }

    public void removeAccount(Account account) {
        this.accounts.remove(account);
    }
}
