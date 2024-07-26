package com.connect.postgresql.Repository;

import com.connect.postgresql.Entity.Job;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.stereotype.Repository;

@Repository
@RedisHash
public interface JobRepository extends JpaRepository<Job, Long> {
}
