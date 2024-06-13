package org.acme;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;
import io.quarkus.runtime.StartupEvent;

import jakarta.enterprise.event.Observes;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.PersistenceException;

import java.util.List;

import org.jboss.logging.Logger;

@ApplicationScoped
public class OperatorService {
    private static final Logger LOGGER = Logger.getLogger(OperatorService.class);

    @PersistenceContext
    EntityManager entityManager;

    @Transactional
    void onStart(@Observes StartupEvent ev) {
        // Executar alguma lógica ao iniciar a aplicação, se necessário
    }

    public List<Operator> getAllOperators() {
        return entityManager.createQuery("SELECT o FROM Operator o", Operator.class).getResultList();
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
}
