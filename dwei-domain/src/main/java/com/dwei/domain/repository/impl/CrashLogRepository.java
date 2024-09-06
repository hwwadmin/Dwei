package com.dwei.domain.repository.impl;

import com.dwei.core.mvc.repository.BaseRepository;
import com.dwei.domain.entity.CrashLogEntity;
import com.dwei.domain.repository.ICrashLogRepository;
import com.dwei.domain.mapper.CrashLogMapper;
import org.springframework.stereotype.Service;

@Service
public class CrashLogRepository extends BaseRepository<CrashLogMapper, CrashLogEntity>
        implements ICrashLogRepository {

}




