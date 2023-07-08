package com.zhangys.carplugin.repository;

import com.zhangys.carplugin.Entity.Po.FixRecords;
import org.springframework.data.repository.CrudRepository;

public interface IssueRepository extends CrudRepository<FixRecords, String> {

}
