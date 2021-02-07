package dp.orm.objects;

import dp.orm.annotations.DatabaseField;
import dp.orm.annotations.DatabaseTable;
import dp.orm.annotations.Id;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@DatabaseTable
public class Animal {

    @Id
    @DatabaseField
    private int id;

    @DatabaseField
    private String type = "";



    @DatabaseField
    private String name;
}
