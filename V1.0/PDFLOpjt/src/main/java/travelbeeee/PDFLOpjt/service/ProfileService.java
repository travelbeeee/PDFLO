package travelbeeee.PDFLOpjt.service;

import travelbeeee.PDFLOpjt.domain.Profile;

public interface ProfileService {
    Profile selectByUserId(int userId);
}
