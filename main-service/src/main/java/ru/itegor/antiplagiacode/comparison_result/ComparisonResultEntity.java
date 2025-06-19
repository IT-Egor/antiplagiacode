package ru.itegor.antiplagiacode.comparison_result;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.proxy.HibernateProxy;
import ru.itegor.antiplagiacode.file.FileEntity;

import java.math.BigDecimal;
import java.util.Objects;

@Getter
@Setter
@Entity
@ToString
@Table(name = "comparison_results",
        uniqueConstraints = {
            @UniqueConstraint(
                    name = "uk_compared_file_id",
                    columnNames = {"original_file_id", "compared_file_id"}
            )
        }
)
public class ComparisonResultEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "result", nullable = false, precision = 5, scale = 2)
    private BigDecimal result;

    @ToString.Exclude
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "original_file_id", nullable = false)
    private FileEntity originalFile;

    @ToString.Exclude
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "compared_file_id", nullable = false)
    private FileEntity comparedFile;

    @Override
    public final boolean equals(Object o) {
        if (this == o) return true;
        if (o == null) return false;
        Class<?> oEffectiveClass = o instanceof HibernateProxy proxy ? proxy.getHibernateLazyInitializer().getPersistentClass() : o.getClass();
        Class<?> thisEffectiveClass = this instanceof HibernateProxy proxy ? proxy.getHibernateLazyInitializer().getPersistentClass() : this.getClass();
        if (thisEffectiveClass != oEffectiveClass) return false;
        ComparisonResultEntity that = (ComparisonResultEntity) o;
        return getId() != null && Objects.equals(getId(), that.getId());
    }

    @Override
    public final int hashCode() {
        return this instanceof HibernateProxy proxy ? proxy.getHibernateLazyInitializer().getPersistentClass().hashCode() : getClass().hashCode();
    }
}