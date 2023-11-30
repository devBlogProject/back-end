package com.multi.blogging.multiblogging.heart.repository;

import com.multi.blogging.multiblogging.heart.domain.Heart;
import com.multi.blogging.multiblogging.heart.repository.custom.CustomHeartRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface HeartRepository extends JpaRepository<Heart,Long>, CustomHeartRepository {
}
