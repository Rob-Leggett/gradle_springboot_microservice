package au.com.example.microservice.payslip;

import au.com.example.microservice.constant.Constant;
import au.com.example.microservice.payslip.model.IncomeSummary;
import au.com.example.microservice.payslip.model.PayslipSummary;
import au.com.example.microservice.payslip.util.IncomeSummaryMapper;
import au.com.example.microservice.payslip.util.PayslipSummaryMapper;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
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

    List<IncomeSummary> incomeSummary = new ArrayList<>();

    InputStream inputStream = null;
    InputStreamReader inputStreamReader = null;
    BufferedReader bufferedReader = null;

    try {
      inputStream = file.getInputStream();
      inputStreamReader = new InputStreamReader(inputStream);
      bufferedReader = new BufferedReader(inputStreamReader);

      incomeSummary = bufferedReader.lines()
              .map(incomeSummaryMapper.mapToIncomeSummary)
              .collect(Collectors.toList());

    } catch (IOException e) {
      LOG.error("Unable to convert file {}", e.getMessage());
    } finally {
      IOUtils.closeQuietly(inputStream);
      IOUtils.closeQuietly(inputStreamReader);
      IOUtils.closeQuietly(bufferedReader);
    }

    return incomeSummary;
  }

  @Override
  public Resource convert(final List<PayslipSummary> payslipSummaries) {

    Resource resource = null;

    ByteArrayOutputStream outputStream = null;
    ByteArrayInputStream inputStream = null;

    try {
      outputStream = new ByteArrayOutputStream();

      List<String> lines = payslipSummaries.stream()
              .map(payslipSummaryMapper.mapToString)
              .collect(Collectors.toList());

      for (String line : lines) {
        outputStream.write(line.getBytes());
      }

      inputStream = new ByteArrayInputStream(outputStream.toByteArray());

      resource = new InputStreamResource(inputStream);
    } catch (IOException e) {
      LOG.error("Unable to write file {}", e.getMessage());
    } finally {
      IOUtils.closeQuietly(outputStream);
      IOUtils.closeQuietly(inputStream);
    }

    return resource;
  }
}
