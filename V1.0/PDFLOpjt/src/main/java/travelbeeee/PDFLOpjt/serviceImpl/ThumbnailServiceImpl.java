package travelbeeee.PDFLOpjt.serviceImpl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import travelbeeee.PDFLOpjt.domain.Thumbnail;
import travelbeeee.PDFLOpjt.repository.ThumbnailRepository;
import travelbeeee.PDFLOpjt.service.ThumbnailService;

@Service @RequiredArgsConstructor
public class ThumbnailServiceImpl implements ThumbnailService {

    private final ThumbnailRepository thumbnailRepository;

    @Override
    public Thumbnail selectById(int thumbnailId) {
        Thumbnail thumbnail = thumbnailRepository.selectById(thumbnailId);

        return thumbnail;
    }
}
