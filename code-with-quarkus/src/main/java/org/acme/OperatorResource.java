package org.acme;

import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.jboss.logging.Logger;

import jakarta.inject.Inject;
import jakarta.persistence.PersistenceException;

import java.util.List;

@Path("/operators")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class OperatorResource {

    private static final Logger LOGGER = Logger.getLogger(OperatorResource.class);

    private final OperatorService operatorService;

    @Inject
    public OperatorResource(OperatorService operatorService) {
        this.operatorService = operatorService;
    }

    @GET
    public Response getAll() {
        List<Operator> operators = operatorService.getAllOperators();
        return Response.ok(operators).build();
    }

    @POST
    @Path("/add")
    public Response create(Operator operator) {
        try {
            LOGGER.info("Recebendo solicitação para criar operador: " + operator);
            Operator persistedOperator = operatorService.addOperator(operator);
            return Response.ok(persistedOperator).build(); // Retorna HTTP 200 e a entidade persistida
        } catch (PersistenceException e) {
            LOGGER.error("Erro ao criar operador", e);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Erro ao criar operador: " + e.getMessage())
                    .build();
        }
    }

    @GET
    @Path("/{id}")
    public Response getById(@PathParam("id") Integer id) {
        Operator operator = operatorService.getOperatorById(id);
        if (operator != null) {
            return Response.ok(operator).build();
        } else {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
    }

    @PUT
    @Path("/{id}")
    public Response update(@PathParam("id") Integer id, Operator operator) {
        LOGGER.info("Recebendo solicitação para atualizar operador com ID " + id + ": " + operator);
        Operator updatedOperator = operatorService.updateOperator(id, operator);
        if (updatedOperator != null) {
            return Response.ok(updatedOperator).build();
        } else {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
    }

    // @DELETE
    // @Path("/{id}")
    // public Response delete(@PathParam("id") Integer id) {
    // LOGGER.info("Recebendo solicitação para deletar operador com ID " + id);
    // boolean deleted = operatorService.deleteOperator(id);
    // if (deleted) {
    // return Response.ok().build();
    // } else {
    // return Response.status(Response.Status.NOT_FOUND).build();
    // }
    // }
    @DELETE
    @Path("/{id}")
    public Response delete(@PathParam("id") Integer id) {
        LOGGER.info("Recebendo solicitação para deletar operador com ID " + id);
        boolean deleted = operatorService.deleteOperator(id);
        if (deleted) {
            return Response.ok().build();
        } else {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("O operador está associado a um time e não pode ser excluído.")
                    .build();
        }
    }
}
