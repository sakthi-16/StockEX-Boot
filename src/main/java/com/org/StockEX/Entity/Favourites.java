package com.org.StockEX.Entity;


import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
@Table(name = "favourites")
public class Favourites {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long collectionId;

    @NotNull
    private Long userId;

    @NotNull
    private String email;

    @NotNull(message = "collection's name can't be empty!")
    private String collectionName;

    private boolean flag=true;



}
