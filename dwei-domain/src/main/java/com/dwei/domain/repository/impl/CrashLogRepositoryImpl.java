package com.dwei.domain.repository.impl;

import com.dwei.core.mvc.repository.BaseRepositoryImpl;
import com.dwei.domain.entity.CrashLogEntity;
import com.dwei.domain.repository.ICrashLogRepository;
import com.dwei.domain.mapper.CrashLogMapper;
import org.springframework.stereotype.Service;

@Service
public class CrashLogRepositoryImpl extends BaseRepositoryImpl<CrashLogMapper, CrashLogEntity>
        implements ICrashLogRepository {

}




