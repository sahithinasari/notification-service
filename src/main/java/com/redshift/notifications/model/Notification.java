package com.redshift.notifications.model;

import lombok.*;

import java.io.Serializable;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Notification implements Serializable {
    private static final long serialVersionUID = 1L;
    private String message;
    private String recipient;
}
