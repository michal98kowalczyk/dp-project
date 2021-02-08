package dp.orm.mapping;

import dp.orm.mapper.ClassInheritanceMapper;
import dp.orm.mapper.InheritanceMapper;
import dp.orm.mapper.SingleInheritanceMapper;

public enum InheritanceMappingType {
    SINGLE_TABLE(SingleInheritanceMapper.class),CLASS_TABLE(ClassInheritanceMapper.class);
//    , CONCRETE_TABLE, CLASS_TABLE;
    private final Class<? extends InheritanceMapper> mappingClass;

    InheritanceMappingType(Class<? extends InheritanceMapper> inheritanceClass) {
        this.mappingClass = inheritanceClass;
    }

    public Class<? extends InheritanceMapper> getMappingClass() {
        return mappingClass;
    }
}
