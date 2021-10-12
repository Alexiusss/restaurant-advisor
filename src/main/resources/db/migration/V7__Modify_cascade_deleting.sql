alter table review_likes
    drop constraint review_likes_review_id_fkey1;

alter table review_likes
    drop constraint review_likes_user_id_fkey,
    add constraint review_likes_user_id_fkey
    foreign key (user_id) references users (id)
    on delete cascade;

alter table review_likes
    drop constraint review_likes_review_id_fkey,
    add constraint review_likes_review_id_fkey
    foreign key (review_id) references reviews (id)
    on delete cascade;

alter table reviews
    add foreign key (user_id) references users (id) on delete cascade;