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
import java.util.List;

@ApplicationScoped
public class OperatorService {
    private static final Logger LOGGER = Logger.getLogger(OperatorService.class);

    @PersistenceContext
    EntityManager entityManager;

    public PagedResponse getAllOperators(PaginationRequestDto request) {
        PanacheQuery<Operator> query = Operator.findAll(
                Sort.by(request.getSort())
                        .direction(request.direction(request.getDir())))
                .page(Page.of(request.getPageNum(), request.getPageSize()));

        List<Operator> operators = query.list();
        long totalRecords = query.count();

        return new PagedResponse(operators, totalRecords, request.getPageNum(), request.getPageSize());
    }

    // Método para buscar todos os operadores (apenas id e name)
    public List<OperatorDto> getAllOperatorsDto() {
        return entityManager.createQuery("SELECT new org.acme.OperatorDto(o.id, o.name) FROM Operator o", OperatorDto.class)
                            .getResultList();
    }

    @Transactional
    public Operator addOperator(Operator operator) {
        try {
            entityManager.persist(operator);
            return operator;
        } catch (Exception e) {
            LOGGER.error("Erro ao persistir operador no banco de dados", e);
            throw new PersistenceException("Falha ao salvar o operador", e);
        }
    }

    public Operator getOperatorById(Integer id) {
        return entityManager.find(Operator.class, id);
    }

    @Transactional
    public Operator updateOperator(Integer id, Operator updatedOperator) {
        Operator operator = entityManager.find(Operator.class, id);
        if (operator != null) {
            operator.setName(updatedOperator.getName());
            operator.setEmail(updatedOperator.getEmail());
            operator.setEmailActive(updatedOperator.getEmailActive());
            operator.setTelephone(updatedOperator.getTelephone());
            operator.setSmsActive(updatedOperator.getSmsActive());
            operator.setTtsActive(updatedOperator.getTtsActive());
            entityManager.persist(operator);
            return operator;
        }
        return null;
    }

    @Transactional
    public boolean deleteOperator(Integer id) {
        Operator operator = entityManager.find(Operator.class, id);
        if (operator != null) {
            entityManager.remove(operator);
            return true;
        }
        return false;
    }

    public static class PagedResponse {
        public List<Operator> operators;
        public long totalRecords;
        public int currentPage;
        public int pageSize;

        public PagedResponse(List<Operator> operators, long totalRecords, int currentPage, int pageSize) {
            this.operators = operators;
            this.totalRecords = totalRecords;
            this.currentPage = currentPage;
            this.pageSize = pageSize;
        }

    }
}
