package com.middleware.common.repository;

import com.middleware.common.constant.enums.OperationType;
import com.middleware.common.model.BaseModel;
import com.middleware.common.model.EntityChangeHistory;
import com.middleware.common.model.TokenInfo;
import com.middleware.common.util.JsonUtil;
import com.middleware.common.util.LogUtil;
import com.middleware.common.util.TokenInfoUtil;
import org.springframework.data.jpa.provider.PersistenceProvider;
import org.springframework.data.jpa.repository.support.CrudMethodMetadata;
import org.springframework.data.jpa.repository.support.JpaEntityInformation;
import org.springframework.data.jpa.repository.support.JpaEntityInformationSupport;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.io.Serializable;
import java.util.Date;

@Transactional(readOnly = true)
public class CustomizedJpaRepositoryImpl<T extends BaseModel, ID extends Serializable>
        extends SimpleJpaRepository<T, ID> implements CustomizedJpaRepository<T, ID> {

    private final JpaEntityInformation<T, ?> entityInformation;
    private final EntityManager em;
    private final PersistenceProvider provider;

    private CrudMethodMetadata metadata;

    public CustomizedJpaRepositoryImpl(JpaEntityInformation<T, ?> entityInformation, EntityManager entityManager) {
        super(entityInformation, entityManager);
        this.entityInformation = entityInformation;
        this.em = entityManager;
        this.provider = PersistenceProvider.fromEntityManager(entityManager);
    }

    public CustomizedJpaRepositoryImpl(Class<T> domainClass, EntityManager em) {
        this(JpaEntityInformationSupport.getEntityInformation(domainClass, em), em);
    }

    @Override
    @Transactional
    public <S extends T> S save(S entity) {
        if (entity.getCreatedDate() == null) {
            entity.setCreatedDate(new Date());
        }
        entity.setModifiedDate(new Date());

        if (entityInformation.isNew(entity)) {
            em.persist(entity);
            generateChangeHistory(entity, OperationType.CREATE);
        } else {
            entity = em.merge(entity);
            generateChangeHistory(entity, OperationType.UPDATE);
        }
        return entity;
    }

    @Override
    @Transactional
    public void delete(T entity) {
        generateChangeHistory(entity, OperationType.DELETE);
        super.delete(entity);
    }

    @Override
    @Transactional
    @Deprecated
    public void restore(ID id) {
        Class domainClass = getDomainClass();
        String className = domainClass.getSimpleName();
        String classPackage = domainClass.getPackage().getName();

        EntityChangeHistory firstEntityChangeHistory = getNewestEntityChangeHistory(id, classPackage, className);

        Assert.notNull(firstEntityChangeHistory, "No entity change history found");
        Assert.isTrue(OperationType.DELETE.equals(firstEntityChangeHistory.getOperationType()), "The newest change is not the delete operation");

        Object entity = JsonUtil.fromJson(firstEntityChangeHistory.getEntityNewContent(), domainClass);

        em.merge(entity);

        generateChangeHistory((T) entity, OperationType.RESTORE);
    }

    private <S extends T> void generateChangeHistory(S entity, OperationType operationType) {
        EntityChangeHistory entityChangeHistory = new EntityChangeHistory();
        entityChangeHistory.setEntityNewContent(JsonUtil.toJson(entity));
        entityChangeHistory.setOperationType(operationType);
        entityChangeHistory.setOperationTime(new Date());
        entityChangeHistory.setEntitySid(entity.getSid());
        entityChangeHistory.setEntityClassName(entity.getClass().getSimpleName());
        entityChangeHistory.setEntityPackageName(entity.getClass().getPackage().getName());

        TokenInfo tokenInfo = TokenInfoUtil.getCurrentTokenInfo();
        if (tokenInfo != null) {
            entityChangeHistory.setAuthType(tokenInfo.getAuthType());
            entityChangeHistory.setClientId(tokenInfo.getClientId());
            entityChangeHistory.setOperatorSid(tokenInfo.getUserSid());
        }

        LogUtil.info(this.getClass(), JsonUtil.toJson(entityChangeHistory));
    }

    private EntityChangeHistory getNewestEntityChangeHistory(ID id, String packageName, String className) {
        Query query = em.createQuery("select ech from EntityChangeHistory ech where ech.entityPackageName = :entityPackageName and ech.entityClassName = :entityClassName and entitySid = :entitySid order by sid desc");
        query.setParameter("entityPackageName", packageName);
        query.setParameter("entityClassName", className);
        query.setParameter("entitySid", id);
        EntityChangeHistory entityChangeHistory = (EntityChangeHistory) query.setMaxResults(1).getSingleResult();
        return entityChangeHistory;
    }
}
