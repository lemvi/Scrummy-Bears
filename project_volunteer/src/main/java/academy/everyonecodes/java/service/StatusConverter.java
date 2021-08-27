package academy.everyonecodes.java.service;

import academy.everyonecodes.java.data.Status;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Service;

@Service
public class StatusConverter implements Converter<String, Status>
{
    @Override
    public Status convert(String value)
    {
        return Status.valueOf(value.toUpperCase());
    }
}
