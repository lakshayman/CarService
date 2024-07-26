package com.connect.postgresql.Repository;

import com.connect.postgresql.Entity.JobCard;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories;
import org.springframework.stereotype.Repository;

@Repository
public interface JobCardRepository extends JpaRepository<JobCard, Long> {
}
