package com.jonghyun.koneps.global.util;

import com.jonghyun.koneps.global.security.user.Member;
import com.jonghyun.koneps.global.security.user.User;
import com.jonghyun.koneps.global.security.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
@RequiredArgsConstructor
public class Util {
    final UserRepository userRepository;

    public String getLoginId() {
        Object principal = SecurityContextHolder
                .getContext()
                .getAuthentication().getPrincipal();

        String loginId = "";
        if (principal instanceof Member) {
            loginId = ((Member) principal).getUsername();
        }

        return loginId;
    }

    /*public String getLoginUserId() {
        Optional<User> user = userRepository.findByUsername(getLoginId());
        return user.map(User::getUsername).orElse("");
    }*/

    /*public String getLoginCompCd() {
        String loginId = Optional.ofNullable(getLoginId()).orElse("");
        Optional<User> user = userRepository.findByLoginId(loginId);
        return user.map(User::getCompCd).orElse("");
    }*/

    public User getLoginUser() {
        Optional<User> user = userRepository.findByUsername(getLoginId());

        return user.orElse(null);
    }

    public void getCollectionSortByModifiedDate(List<Map<String, Object>> data, String sorting) {
        Collections.sort(data, new Comparator<Map<String, Object>>() {
            public int compare(Map<String, Object> o1, Map<String, Object> o2) {
                String name1 = String.valueOf(o1.get("modifiedDate"));
                String name2 = String.valueOf(o2.get("modifiedDate"));
                if("d".equals(sorting)) {
                    // descending
                    return name2.compareTo(name1);
                } else {
                    // ascending
                    return name1.compareTo(name2);
                }
            }
        });
    }

    public static boolean isNumeric(String strNum) {
        if (strNum == null) {
            return false;
        }
        try {
            int i = Integer.parseInt(strNum);
            float f = Float.parseFloat(strNum);
            double d = Double.parseDouble(strNum);
            long l = Long.parseLong(strNum);
        } catch (NumberFormatException nfe) {
            return false;
        }
        return true;
    }
}
