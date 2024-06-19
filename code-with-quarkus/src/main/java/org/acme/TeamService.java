package org.acme;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;
import io.quarkus.hibernate.orm.panache.PanacheQuery;
import io.quarkus.panache.common.Page;
import io.quarkus.panache.common.Sort;
import org.jboss.logging.Logger;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.PersistenceException;

import java.util.ArrayList;
import java.util.List;

@ApplicationScoped
public class TeamService {
    private static final Logger LOGGER = Logger.getLogger(TeamService.class);

    @PersistenceContext
    EntityManager entityManager;

    public PagedResponse getAllTeams(PaginationRequestDto request) {

        PanacheQuery<Team> query = Team.findAll(
                Sort.by(request.getSort())
                        .direction(request.direction(request.getDir())))
                .page(Page.of(request.getPageNum(), request.getPageSize()));

        List<Team> teams = query.list();
        long totalRecords = query.count();
        System.out.println("getAllTeams totalRecords: " + totalRecords); // Verifica o total de registros

        return new PagedResponse(teams, totalRecords, request.getPageNum(), request.getPageSize());
    }

    @Transactional
    public Team addTeam(Team team) {
        try {
            System.out.println("aaaaaaaaaa: "+team);
            entityManager.persist(team);
            return team;
        } catch (Exception e) {
            LOGGER.error("Erro ao persistir time no banco de dados", e);
            throw new PersistenceException("Falha ao salvar o time", e);
        }
    }

    public Team getTeamById(Integer id) {
        LOGGER.info("Recebendo solicitação ver time com id: " + id);
        System.out.println("entrou getTeamById: " + id); // Verifica o total de registros
        return entityManager.find(Team.class, id);
    }

    @Transactional
    public Team updateTeam(Integer id, Team updatedTeam) {
        System.out.println("entrou updateTeam: " + id); // Verifica o total de registros

        Team team = entityManager.find(Team.class, id);
        if (team != null) {
            team.setName(updatedTeam.getName());
            team.setOperators(updatedTeam.getOperators());
            entityManager.persist(team);
            return team;
        }
        return null;
    }

    @Transactional
    public boolean deleteTeam(Integer id) {
        Team team = entityManager.find(Team.class, id);
        if (team != null) {
            entityManager.remove(team);
            return true;
        }
        return false;
    }

    public static class PagedResponse {
        public List<Team> teams;
        public long totalRecords;
        public int currentPage;
        public int pageSize;

        public PagedResponse(List<Team> teams, long totalRecords, int currentPage, int pageSize) {
            this.teams = teams;
            this.totalRecords = totalRecords;
            this.currentPage = currentPage;
            this.pageSize = pageSize;
        }
    }
}
