package dp.orm.objects.players;

import dp.orm.annotations.DatabaseField;
import dp.orm.annotations.DatabaseTable;
import dp.orm.mapping.InheritanceMappingType;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;


@Getter
@Setter
@ToString
@DatabaseTable(inheritanceType = InheritanceMappingType.CONCRETE_TABLE)
public class Bowler  extends Cricketer{

    @DatabaseField
    Double bowlingAverage;
}
