package org.acme;

import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.ArrayList;
import java.util.List;

import org.jboss.logging.Logger;
import jakarta.inject.Inject;
import jakarta.persistence.PersistenceException;

import org.acme.OperatorService;

@Path("/teams")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class TeamResource {

    private static final Logger LOGGER = Logger.getLogger(TeamResource.class);

    private final TeamService teamService;
    private final OperatorService operatorService;

    @Inject
    public TeamResource(TeamService teamService, OperatorService operatorService) {
        this.teamService = teamService;
        this.operatorService = operatorService;
    }

    @GET
    public Response getAll(@BeanParam PaginationRequestDto request) {
        try {
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
        LOGGER.info("Recebendo solicitação para criar time: " + team.toString());
        // Verificando os operadores recebidos
        List<Operator> operatorss = team.getOperators();
        if (operatorss != null) {
            for (Operator operator : operatorss) {
                LOGGER.info("Operador recebido: " + operator.getId());
                // Aqui você pode adicionar mais detalhes do operador, se necessário
            }
        } else {
            LOGGER.info("Nenhum operador recebido.");
        }

        try {
            // Aqui você pode iterar pelos IDs dos operadores e buscar cada um no banco de
            // dados
            List<Operator> operators = team.getOperators();

            List<Operator> resolvedOperators = new ArrayList<>();

            for (Operator operator : operators) {
                Operator resolvedOperator = operatorService.getOperatorById(operator.getId());

                if (resolvedOperator != null) {

                    resolvedOperators.add(resolvedOperator);
                } else {
                    // Se o operador com o ID especificado não for encontrado, você pode decidir
                    // como lidar com isso
                    // Exemplo: lançar uma exceção ou retornar um erro específico
                    return Response.status(Response.Status.BAD_REQUEST)
                            .entity("Operador com ID " + operator.getId() + " não encontrado")
                            .build();
                }
            }

            // Defina os operadores associados ao time
            team.setOperators(resolvedOperators);

            // Chama o método do service para persistir o time
            Team persistedTeam = teamService.addTeam(team);
            return Response.ok(persistedTeam).build();
        } catch (PersistenceException e) {
            System.err.println("Erro ao persistir time no banco de dados: " + e.getMessage());
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Falha ao salvar o time")
                    .build();
        } catch (Exception e) {
            System.err.println("Erro ao criar time: " + e.getMessage());
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
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("O time está associado a operadores e não pode ser excluído.")
                    .build();
        }
    }
}
