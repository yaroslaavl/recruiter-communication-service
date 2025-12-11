CREATE TABLE IF NOT EXISTS communication_data.video (
                                                id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
                                                recruiter_id VARCHAR(255) NOT NULL,
                                                candidate_id VARCHAR(255) NOT NULL,
                                                application_id UUID NOT NULL,
                                                pin VARCHAR(10) NOT NULL,
                                                pin_expires_at TIMESTAMP WITHOUT TIME ZONE NOT NULL,
                                                recruiter_joined BOOLEAN DEFAULT FALSE NOT NULL,
                                                candidate_joined BOOLEAN DEFAULT FALSE NOT NULL,
                                                status VARCHAR(20) NOT NULL,
                                                created_at TIMESTAMP NOT NULL DEFAULT now()
);
