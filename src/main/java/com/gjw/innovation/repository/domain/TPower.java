package com.gjw.innovation.repository.domain;

import lombok.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.*;

/**
 * 权限表
 */
@Data
@Entity
public class TPower {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private String powerValue;
}