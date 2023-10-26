package com.multi.blogging.multiblogging.board.repository.custom.impl;

import com.multi.blogging.multiblogging.board.repository.custom.CustomBookRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
@Qualifier("customBookRepositoryImpl")
public class CustomBookRepositoryImpl implements CustomBookRepository {
}
