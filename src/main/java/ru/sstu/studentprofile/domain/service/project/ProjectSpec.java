package ru.sstu.studentprofile.domain.service.project;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Root;
import jakarta.persistence.criteria.Subquery;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import ru.sstu.studentprofile.data.models.project.ActualRoleForProject;
import ru.sstu.studentprofile.data.models.project.Project;

import java.util.Objects;

public class ProjectSpec {
    private ProjectSpec() {
    }

    public static Specification<Project> filterBy(final String searchQuery,
                                                  final boolean needActualRoles,
                                                  final long userId,
                                                  Sort.Direction orderBy) {

        return (root, query, cb) -> {
            root.join("actualRoleForProject", JoinType.LEFT);
            root.join("projectMembers", JoinType.LEFT);
            // Добавили distinct, чтобы избежать дубликатов
            query.distinct(true);

            orderHandler(root, query, cb, orderBy);

            return cb.and(
                    searchQuery(searchQuery).toPredicate(root, query, cb),
                    needActualRoles(needActualRoles).toPredicate(root, query, cb),
                    byUserId(userId).toPredicate(root, query, cb)

            );
        };
    }



    private static Specification<Project> searchQuery(final String searchQuery) {
        return (root, query, cb) -> {
            if (searchQuery == null || searchQuery.isEmpty()) {
                return cb.conjunction();
            }
            return cb.like(cb.lower(root.get("name")), "%" + searchQuery.toLowerCase() + "%");
        };
    }

    private static Specification<Project> needActualRoles(final boolean needActualRoles) {
        return (root, query, cb) -> {
            if (needActualRoles) {
                Subquery<Long> subquery = query.subquery(Long.class);
                Root<ActualRoleForProject> subRoot = subquery.from(ActualRoleForProject.class);
                subquery.select(cb.literal(1L))
                        .where(cb.equal(subRoot.get("project").get("id"), root.get("id")));

                // Проверяем, что подзапрос вернул не пустой результат
                return cb.exists(subquery);
            }
            return cb.conjunction();
        };
    }

    private static Specification<Project> byUserId(final long userId) {
        return (root, query, cb) -> {
            if (userId != 0) {
                return root.get("projectMembers").get("user").get("id").in(userId);
            }
            return cb.conjunction();
        };
    }

    private static void orderHandler(final Root<Project> root,
                                     final CriteriaQuery<?> query,
                                     final CriteriaBuilder cb,
                                     final Sort.Direction orderBy) {
        if (Objects.requireNonNull(orderBy) == Sort.Direction.ASC) {
            query.orderBy(cb.asc(root.get("id")));
        } else {
            query.orderBy(cb.desc(root.get("id")));
        }
    }

}
