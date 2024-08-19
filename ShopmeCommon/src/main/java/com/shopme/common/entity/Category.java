package com.shopme.common.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@ToString(exclude = {"parent", "children"})
@EqualsAndHashCode(of = "id")
@NoArgsConstructor
@Builder
@Entity
@Table(name = "categories")
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 128, nullable = false, unique = true)
    private String name;

    @Column(length = 64, nullable = false, unique = true)
    private String alias;

    @Column(length = 128, nullable = false)
    private String image;

    private Boolean enabled;

    @ManyToOne
    @JoinColumn(name = "parent_id")
    private Category parent;

    @OneToMany(mappedBy = "parent")
    @Builder.Default
    @Fetch(FetchMode.SUBSELECT)
    private List<Category> children = new ArrayList<>();

    public void addParent(Category parent) {
        this.parent = parent;
        parent.getChildren().add(this);
    }

    @Transient
    public String getImagePath() {
        return "/category-images/" + this.id + "/" + this.image;
    }

}
