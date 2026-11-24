package com.eazybytes.cards.service.Impl;

import com.eazybytes.cards.constants.CardsConstant;
import com.eazybytes.cards.dto.CardsDto;
import com.eazybytes.cards.entity.Cards;
import com.eazybytes.cards.exception.CardAlreadyExistsException;
import com.eazybytes.cards.exception.ResourceNotFoundException;
import com.eazybytes.cards.mapper.CardsMapper;
import com.eazybytes.cards.repository.CardsRepository;
import com.eazybytes.cards.service.ICardsService;
import io.micrometer.core.instrument.MeterRegistry;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.Random;

@Service
@AllArgsConstructor
public class CardsServiceImpl implements ICardsService {

    private static final Logger logger = LoggerFactory.getLogger(CardsServiceImpl.class);

    private CardsRepository cardsRepository;
    private MeterRegistry meterRegistry;

    @Override
    public void createCard(String mobileNumber) {
        logger.debug("Creating card");
        Optional<Cards> cards = cardsRepository.findByMobileNumber(mobileNumber);
        if(cards.isPresent()) {
            throw new CardAlreadyExistsException("Card already exists with mobile number " + mobileNumber);
        }
        cardsRepository.save(createNewCard(mobileNumber));
        logger.info("Card created successfully");
        meterRegistry.counter("cards.operations", "operation", "created").increment();
    }

    @Override
    public CardsDto fetchCard(String mobileNumber) {
        logger.debug("Fetching card details");
        Cards cards = cardsRepository.findByMobileNumber(mobileNumber).orElseThrow(() -> new ResourceNotFoundException("Card not found with mobile number " + mobileNumber));
        logger.debug("Card details fetched successfully");
        return CardsMapper.mapCardsToCardsDto(cards, new CardsDto());
    }

    @Override
    public boolean updateCard(CardsDto cardsDto) {
        logger.debug("Updating card details");
        Cards cards = cardsRepository.findByCardNumber(cardsDto.getCardNumber()).orElseThrow(() ->  new ResourceNotFoundException("Card not found with card number " + cardsDto.getCardNumber()));
        CardsMapper.mapCardsDtoToCards(cardsDto, cards);
        cardsRepository.save(cards);
        logger.info("Card details updated successfully");
        meterRegistry.counter("cards.operations", "operation", "updated").increment();
        return true;
    }

    @Override
    public boolean deleteCard(String mobileNumber) {
        logger.debug("Deleting card");
        Cards cards = cardsRepository.findByMobileNumber(mobileNumber).orElseThrow(() -> new ResourceNotFoundException("Card not found with mobile number " + mobileNumber));
        cardsRepository.delete(cards);
        logger.info("Card deleted successfully");
        meterRegistry.counter("cards.operations", "operation", "deleted").increment();
        return true;
    }

    private Cards createNewCard(String mobileNumber) {
        Cards newCard = new Cards();
        long randomCardNumber = 100000000000L + new Random().nextInt(900000000);
        newCard.setCardNumber(Long.toString(randomCardNumber));
        newCard.setMobileNumber(mobileNumber);
        newCard.setCardType(CardsConstant.CREDIT_CARD);
        newCard.setTotalLimit(CardsConstant.NEW_CARD_LIMIT);
        newCard.setAmountUsed(0);
        newCard.setAvailableAmount(CardsConstant.NEW_CARD_LIMIT);
        return newCard;
    }
}
