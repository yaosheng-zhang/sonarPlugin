package com.zhangys.carplugin.repository;

import com.zhangys.carplugin.Entity.FixRecords;
import org.springframework.data.repository.CrudRepository;

public interface IssueRepository extends CrudRepository<FixRecords, String> {

}
