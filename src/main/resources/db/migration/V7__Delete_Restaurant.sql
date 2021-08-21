--https://fooobar.com/questions/2412357/spring-data-jpa-how-to-enable-cascading-delete-without-a-reference-to-the-child-in-the-parent/6260940#6260940

alter table contacts drop constraint fknde9mdcjsx5apyb1ba2971svs;

alter table contacts
    add constraint fknde9mdcjsx5apyb1ba2971svs
        foreign key (restaurant_id) references restaurants
            on delete cascade;

alter table reviews
    add constraint reviews_restaurants__fk
        foreign key (restaurant_id) references restaurants (id)
            on delete cascade;

alter table review_likes drop constraint review_likes_review_id_fkey;

alter table review_likes
    add constraint review_likes_review_id_fkey
        foreign key (review_id) references reviews (id)
            on delete cascade;