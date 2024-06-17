package org.acme;

import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.jboss.logging.Logger;
import jakarta.inject.Inject;
import jakarta.persistence.PersistenceException;

@Path("/teams")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class TeamResource {

    private static final Logger LOGGER = Logger.getLogger(TeamResource.class);

    private final TeamService teamService;

    @Inject
    public TeamResource(TeamService teamService) {
        this.teamService = teamService;
    }

    @GET
    public Response getAll(@BeanParam PaginationRequestDto request) {
        try {
            // System.out.println("getAll request: " + request); // Verifica os parâmetros recebidos
            // System.out.println("getAll dir: " + request.getDir());
            // System.out.println("getAll pageNum: " + request.getPageNum());
            // System.out.println("getAll pageSize: " + request.getPageSize());
            // System.out.println("getAll Sort: " + request.getSort());

            TeamService.PagedResponse pagedResponse = teamService.getAllTeams(request);
            return Response.ok(pagedResponse).build();
        } catch (Exception e) {
            LOGGER.error("Erro ao buscar times", e);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Erro ao buscar times: " + e.getMessage())
                    .build();
        }
    }

    @POST
    @Path("/add")
    public Response create(Team team) {
        try {
            LOGGER.info("Recebendo solicitação para criar time: " + team);
            Team persistedTeam = teamService.addTeam(team);
            return Response.ok(persistedTeam).build(); // Retorna HTTP 200 e a entidade persistida
        } catch (PersistenceException e) {
            LOGGER.error("Erro ao criar time", e);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Erro ao criar time: " + e.getMessage())
                    .build();
        }
    }

    @GET
    @Path("/{id}")
    public Response getById(@PathParam("id") Integer id) {
        Team team = teamService.getTeamById(id);
        if (team != null) {
            return Response.ok(team).build();
        } else {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
    }

    @PUT
    @Path("/{id}")
    public Response update(@PathParam("id") Integer id, Team team) {
        LOGGER.info("Recebendo solicitação para atualizar time com ID " + id + ": " + team);
        Team updatedTeam = teamService.updateTeam(id, team);
        if (updatedTeam != null) {
            return Response.ok(updatedTeam).build();
        } else {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
    }

    @DELETE
    @Path("/{id}")
    public Response delete(@PathParam("id") Integer id) {
        LOGGER.info("Recebendo solicitação para deletar time com ID " + id);
        boolean deleted = teamService.deleteTeam(id);
        if (deleted) {
            return Response.ok().build();
        } else {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("O time está associado a operadores e não pode ser excluído.")
                    .build();
        }
    }
}
