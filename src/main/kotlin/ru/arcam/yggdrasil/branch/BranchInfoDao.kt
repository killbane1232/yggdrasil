package ru.arcam.yggdrasil.branch

import org.springframework.jdbc.core.namedparam.MapSqlParameterSource
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import org.springframework.stereotype.Repository

@Repository
class BranchInfoDao(
    val jdbcTemplate: NamedParameterJdbcTemplate
) {

    fun createBranch(branchInfo: BranchInfo) {
        jdbcTemplate.update(
            "INSERT INTO branch_info (service_name) VALUES (:service_name) ",
            MapSqlParameterSource("service_name", branchInfo.serviceName)
        )
    }

    fun getBranchByServiceName(serviceName: String?): BranchInfo? {
        return jdbcTemplate.queryForObject(
            "SELECT * FROM branch_info WHERE service_name = :service_name",
            MapSqlParameterSource("service_name", serviceName),
            BranchInfoRowMapper()
        )
    }

    fun deleteBranch(serviceName: String?) {
        jdbcTemplate.update(
            "DELETE FROM branch_info WHERE service_name = :service_name",
            MapSqlParameterSource("service_name", serviceName)
        )
    }
}