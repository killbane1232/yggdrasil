package ru.arcam.yggdrasil.leaf

//import org.springframework.jdbc.core.namedparam.MapSqlParameterSource
//import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import org.springframework.stereotype.Repository

@Repository
class LeafInfoDao(
    // val jdbcTemplate: NamedParameterJdbcTemplate
) {

   /* fun createBranch(leaf: Leaf) {
        jdbcTemplate.update(
            "INSERT INTO leaf_info (name, attached_branch) VALUES (:name, :attached_branch) ",
            MapSqlParameterSource("name", leaf.name).addValue("attached_branch", leaf.attachedBranch)
        )
    }

    fun getBranchByServiceName(name: String?): Leaf? {
        return jdbcTemplate.queryForObject(
            "SELECT * FROM leaf_info WHERE name = :name",
            MapSqlParameterSource("name", name),
            LeafRowMapper()
        )
    }

    fun deleteBranch(name: String?) {
        jdbcTemplate.update(
            "DELETE FROM leaf_info WHERE name = :name",
            MapSqlParameterSource("name", name)
        )
    }*/
}