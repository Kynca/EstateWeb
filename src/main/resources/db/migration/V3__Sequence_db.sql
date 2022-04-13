drop sequence if exists address_sequence;
drop sequence if exists flat_sequence;
drop sequence if exists real_estate_sequence;
drop sequence if exists user_sequence;

create sequence address_sequence start 26 increment 1;
create sequence flat_sequence start 14 increment 1;
create sequence real_estate_sequence start 26 increment 1;
create sequence user_sequence start 6 increment 1;