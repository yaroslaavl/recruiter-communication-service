CREATE TABLE IF NOT EXISTS chat (
                                    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
                                    created_at TIMESTAMP NOT NULL DEFAULT now()
);

CREATE TABLE IF NOT EXISTS chat_participant (
                                                id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
                                                chat_id UUID NOT NULL REFERENCES chat(id) ON DELETE CASCADE,
                                                user_id VARCHAR(255) NOT NULL
);

CREATE TABLE IF NOT EXISTS chat_message (
                                            id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
                                            chat_id UUID NOT NULL REFERENCES chat(id) ON DELETE CASCADE,
                                            sender_id VARCHAR(255) NOT NULL,
                                            content TEXT NOT NULL,
                                            status VARCHAR(20) NOT NULL,
                                            created_at TIMESTAMP NOT NULL DEFAULT now()
);
