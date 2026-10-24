package au.com.example.microservice.payslip;

import au.com.example.microservice.constant.Constant;
import au.com.example.microservice.payslip.model.IncomeSummary;
import au.com.example.microservice.payslip.model.PayslipSummary;
import au.com.example.microservice.payslip.util.IncomeSummaryMapper;
import au.com.example.microservice.payslip.util.PayslipSummaryMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PayslipFileServiceImpl implements PayslipFileService {

  private static final Logger LOG = LoggerFactory.getLogger(PayslipFileServiceImpl.class);

  @Autowired
  private IncomeSummaryMapper incomeSummaryMapper;

  @Autowired
  private PayslipSummaryMapper payslipSummaryMapper;

  @Override
  public boolean isSupportedFileType(String fileType) {
    return (Constant.TEXT_CSV.equals(fileType) || Constant.APPLICATION_CSV.equals(fileType));
  }

  @Override
  public List<IncomeSummary> convert(final MultipartFile file) {

    try (BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(file.getInputStream(), StandardCharsets.UTF_8))) {
      return bufferedReader.lines()
              .map(incomeSummaryMapper.mapToIncomeSummary)
              .collect(Collectors.toList());
    } catch (IOException e) {
      LOG.error("Unable to convert file {}", e.getMessage());
      return new ArrayList<>();
    }
  }

  @Override
  public Resource convert(final List<PayslipSummary> payslipSummaries) {

    final String content = payslipSummaries.stream()
            .map(payslipSummaryMapper.mapToString)
            .collect(Collectors.joining());

    return new ByteArrayResource(content.getBytes(StandardCharsets.UTF_8));
  }
}
