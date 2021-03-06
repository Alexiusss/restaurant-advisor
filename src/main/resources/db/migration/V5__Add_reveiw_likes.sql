create table review_likes
(
    user_id   int4 not null references users,
    review_id int4 not null references reviews,
    primary key (user_id, review_id),
    foreign key (review_id) references reviews (id) on delete cascade
)